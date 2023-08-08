package gay.lemmaeof.mycoturgy.data.criterion;

import com.google.gson.JsonObject;
import gay.lemmaeof.mycoturgy.Mycoturgy;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.unmapped.C_ctsfmifk;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class MeditateCriterion extends AbstractCriterion<MeditateCriterion.Conditions> {
	public static final Identifier ID = new Identifier(Mycoturgy.MODID, "meditate");

	@Override
	protected Conditions conditionsFromJson(JsonObject obj, /*PlayerPredicate*/C_ctsfmifk playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		Identifier path = new Identifier(JsonHelper.getString(obj, "path", "mycoturgy:aimless"));
		return new Conditions(path);
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	public void trigger(ServerPlayerEntity player, Identifier id) {
		trigger(player, conditions -> conditions.matches(id));
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final Identifier path;

		public Conditions(Identifier path) {
			super(ID, /*PlayerPredicate*/C_ctsfmifk.field_24388);
			this.path = path;
		}

		public boolean matches(Identifier id) {
			return id.equals(path);
		}
	}
}
