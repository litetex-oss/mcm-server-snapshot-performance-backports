package net.litetex.sspb.mixin;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.logging.LogUtils;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerNetworkIo;
import net.minecraft.server.ServerTickManager;
import net.minecraft.util.profiler.Profiler;


/**
 * Backports the pause-when-empty-seconds functionality
 */
@Mixin(net.minecraft.server.MinecraftServer.class)
public abstract class MinecraftServerPWEMixin
{
	@Unique
	private static final Logger LOGGER = LogUtils.getLogger();
	
	@Unique
	private int idleTickCount;
	
	@Inject(
		method = "tick",
		at = @At(value = "HEAD"),
		cancellable = true
	)
	public void tick(final CallbackInfo ci)
	{
		final int pauseWhenEmptyTicks = this.getPauseWhenEmptySeconds() * 20;
		if(pauseWhenEmptyTicks > 0)
		{
			this.idleTickCount = this.playerManager.getCurrentPlayerCount() == 0 && !this.tickManager.isSprinting()
				? ++this.idleTickCount
				: 0;
			if(this.idleTickCount >= pauseWhenEmptyTicks)
			{
				if(this.idleTickCount == pauseWhenEmptyTicks)
				{
					LOGGER.info("Server empty for {} seconds, pausing", this.getPauseWhenEmptySeconds());
					this.runAutoSave();
				}
				
				this.tickNetworkIo();
				ci.cancel();
			}
		}
	}
	
	@Unique
	public int getPauseWhenEmptySeconds()
	{
		return 0;
	}
	
	@Unique
	void runAutoSave()
	{
		this.ticksUntilAutosave = this.getAutosaveInterval();
		LOGGER.debug("Autosave started");
		this.profiler.push("save");
		this.saveAll(true, false, false);
		this.profiler.pop();
		LOGGER.debug("Autosave finished");
	}
	
	@Unique
	void tickNetworkIo()
	{
		this.getNetworkIo().tick();
	}
	
	@Shadow
	private PlayerManager playerManager;
	
	@Shadow
	@Final
	private ServerTickManager tickManager;
	
	@Shadow
	protected abstract int getAutosaveInterval();
	
	@Shadow
	private int ticksUntilAutosave;
	
	@Shadow
	public abstract ServerNetworkIo getNetworkIo();
	
	@Shadow
	public abstract boolean saveAll(final boolean suppressLogs, final boolean flush, final boolean force);
	
	@Shadow
	private Profiler profiler;
}
