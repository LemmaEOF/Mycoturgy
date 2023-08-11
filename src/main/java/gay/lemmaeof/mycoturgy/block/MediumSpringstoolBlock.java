package gay.lemmaeof.mycoturgy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;

public class MediumSpringstoolBlock extends SpringstoolBlock{
	private static final IntProperty X = IntProperty.of("x", 0, 1);
	private static final IntProperty Z = IntProperty.of("z", 0, 1);

	public MediumSpringstoolBlock( Settings settings) {
		super(1, X, Z, settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(X, Z);
	}
}
