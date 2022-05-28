package gay.lemmaeof.mycoturgy.data.criterion;

import com.google.gson.JsonObject;
import gay.lemmaeof.mycoturgy.Mycoturgy;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ManualCriterion extends AbstractCriterion<ManualCriterion.Conditions> {
	private final Identifier id;

	public ManualCriterion(String id) {
		this.id = new Identifier(Mycoturgy.MODID, id);
	}

	@Override
	protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new Conditions(playerPredicate);
	}

	@Override
	public Identifier getId() {
		return id;
	}

	public void trigger(ServerPlayerEntity player) {
		trigger(player, (cond) -> true);
	}

	public class Conditions extends AbstractCriterionConditions {

		public Conditions(EntityPredicate.Extended extended) {
			super(id, extended);
		}
	}
}
