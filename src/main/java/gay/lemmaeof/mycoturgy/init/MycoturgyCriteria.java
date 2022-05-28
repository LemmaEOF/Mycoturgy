package gay.lemmaeof.mycoturgy.init;

import gay.lemmaeof.mycoturgy.data.criterion.ManualCriterion;
import gay.lemmaeof.mycoturgy.data.criterion.MeditateCriterion;

import net.minecraft.advancement.criterion.Criteria;

public class MycoturgyCriteria {
	public static final MeditateCriterion MEDITATE = Criteria.register(new MeditateCriterion());
	public static final ManualCriterion BIG_HIT = Criteria.register(new ManualCriterion("big_hit"));
	public static final ManualCriterion CAST_SPELL = Criteria.register(new ManualCriterion("cast_spell"));
	public static final ManualCriterion EQUIP_BAND = Criteria.register(new ManualCriterion("equip_band"));
	public static final ManualCriterion FEEL_GRIEF = Criteria.register(new ManualCriterion("feel_grief"));
	public static final ManualCriterion SHARE_PIPE = Criteria.register(new ManualCriterion("share_pipe"));

	public static void init() {

	}
}
