package gay.lemmaeof.mycoturgy.data.meditate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import gay.lemmaeof.mycoturgy.Mycoturgy;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import net.minecraft.advancement.Advancement;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;

public class MeditationManager extends JsonDataLoader implements IdentifiableResourceReloader {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	public static final MeditationManager INSTANCE = new MeditationManager();
	public static final Identifier ID = new Identifier(Mycoturgy.MODID, "meditations");
	private static final Map<Identifier, MeditationStep> roots = new HashMap<>();

	public MeditationManager() {
		super(GSON, "meditations");
	}

	public Map<Identifier, MeditationStep> getRoots() {
		return roots;
	}

	public Map<Identifier, MeditationStep> getMeditationsFor(ServerPlayerEntity player) {
		Map<Identifier, MeditationStep> ret = new HashMap<>();
		for (Identifier id : roots.keySet()) {
			MeditationStep step = roots.get(id);
			if (step.canFollow(player)) {
				ret.put(id, step.prepareFor(player));
			}
		}
		return ret;
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		roots.clear();
		for (Identifier id : prepared.keySet()) {
			roots.put(id, deserializeStep(JsonHelper.asObject(prepared.get(id), "top element")));
		}
		Mycoturgy.LOGGER.info("Loaded {} meditation root{}", roots.size(), roots.size() == 1? "" : "s");
	}

	private MeditationStep deserializeStep(JsonObject json) throws JsonParseException {
		Identifier icon = JsonHelper.hasString(json, "icon")? new Identifier(JsonHelper.getString(json, "icon")) : null;
		ItemStack stack = null;
		if (JsonHelper.hasString(json, "item")) {
			stack = new ItemStack(Registry.ITEM.get(new Identifier(JsonHelper.getString(json, "item"))));
		} else if (JsonHelper.hasJsonObject(json, "item")) {
			JsonObject itemObj = JsonHelper.getObject(json, "item");
			if (JsonHelper.hasJsonObject(itemObj, "data")) {
				JsonObject dataObj = JsonHelper.getObject(itemObj, "data");
				itemObj.remove("data");
				stack = ShapedRecipe.outputFromJson(itemObj);
				NbtElement elem = JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, dataObj);
				if (elem instanceof NbtCompound nbt) {
					stack.setNbt(nbt);
				} else {
					throw new JsonParseException("Stack data must be an NBT compound! How did this even crash here?");
				}
			} else {
				stack = ShapedRecipe.outputFromJson(itemObj);
			}
		}
		Text description = null;
		if (json.has("description")) {
			description = Text.Serializer.fromJson(json.get("description"));
		}
		List<MeditationRequirement> requirements = new ArrayList<>();
		JsonObject reqObj = JsonHelper.getObject(json, "requires", new JsonObject());
		//TODO: more requirement types! Maybe custom ones?
		JsonArray advancementReqs = JsonHelper.getArray(reqObj, "advancements", new JsonArray());
		for (JsonElement elem : advancementReqs) {
			requirements.add(
					new AdvancementRequirement(
							new Identifier(JsonHelper.asString(elem, "advancements array entry"))
					)
			);
		}
		Map<String, MeditationStep> paths = new HashMap<>();
		JsonObject pathsObj = JsonHelper.getObject(json, "paths", new JsonObject());
		for (String key : pathsObj.keySet()) {
			paths.put(key, deserializeStep(JsonHelper.asObject(pathsObj.get(key), key)));
		}
		Identifier destination = JsonHelper.hasString(json, "destination")? new Identifier(JsonHelper.getString(json, "destination")) : null;
		if (paths.isEmpty() == (destination == null)) {
			throw new JsonParseException("Meditation step must have either paths or a destination, not neither or both!");
		}
		return new MeditationStep(icon, stack, description, requirements, paths, destination);
	}

	@Override
	public Identifier getQuiltId() {
		return ID;
	}

	public record MeditationStep(@Nullable Identifier icon, @Nullable ItemStack stack,
								 @Nullable Text description,
								 List<MeditationRequirement> requirements,
								 Map<String, MeditationStep> paths,
								 @Nullable Identifier destination) {
		public boolean canFollow(ServerPlayerEntity player) {
			for (MeditationRequirement req : requirements) {
				if (!req.fulfills(player)) return false;
			}
			return true;
		}

		public MeditationStep prepareFor(ServerPlayerEntity player) {
			Map<String, MeditationStep> newPaths = new HashMap<>();
			for (String path : paths.keySet()) {
				MeditationStep step = paths.get(path);
				if (step.canFollow(player)) {
					newPaths.put(path, step.prepareFor(player));
				}
			}
			return new MeditationStep(icon, stack, description, requirements, newPaths, destination);
		}
	}

	public interface MeditationRequirement {
		boolean fulfills(ServerPlayerEntity player);
	}

	public record AdvancementRequirement(Identifier advancementId) implements MeditationRequirement {

		@Override
		public boolean fulfills(ServerPlayerEntity player) {
			Advancement advancement = player.getServer().getAdvancementLoader().get(advancementId);
			return player.getAdvancementTracker().getProgress(advancement).isDone();
		}
	}
}
