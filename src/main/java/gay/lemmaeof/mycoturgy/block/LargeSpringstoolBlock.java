package gay.lemmaeof.mycoturgy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;

public class LargeSpringstoolBlock extends SpringstoolBlock {
	private static final IntProperty X = IntProperty.of("x", 0, 2);
	private static final IntProperty Z = IntProperty.of("z", 0, 2);

	public LargeSpringstoolBlock( Settings settings) {
		super(1.25, X, Z, settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(X, Z);
	}
}
