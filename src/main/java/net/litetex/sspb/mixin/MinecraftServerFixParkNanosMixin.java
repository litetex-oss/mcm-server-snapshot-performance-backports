package net.litetex.sspb.mixin;

import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.util.Util;
import net.minecraft.util.thread.ThreadExecutor;


/**
 * Backport of fix for MC-183518
 */
@Mixin(net.minecraft.server.MinecraftServer.class)
public abstract class MinecraftServerFixParkNanosMixin
{
	@Unique
	private boolean waitingForNextTickNew;
	
	/**
	 * @author litetex
	 * @reason Backport of fix for MC-183518
	 */
	@Overwrite
	@SuppressWarnings("rawtypes")
	public void runTasksTillTickEnd()
	{
		((ThreadExecutor)(Object)this).runTask();
		this.waitingForNextTickNew = true;
		try
		{
			this.runTasks(() -> !this.shouldKeepTicking());
		}
		finally
		{
			this.waitingForNextTickNew = false;
		}
	}
	
	/**
	 * @author litetex
	 * @reason Backport of fix for MC-183518
	 */
	@SuppressWarnings("checkstyle:MagicNumber")
	@Overwrite
	public void waitForTasks()
	{
		final boolean pushTickTimeLog = this.shouldPushTickTimeLog();
		final long beforeParkNanosMeasuringTimeNano = pushTickTimeLog ? Util.getMeasuringTimeNano() : 0L;
		LockSupport.parkNanos(
			"waiting for tasks",
			this.waitingForNextTickNew ? this.tickStartTimeNanos - Util.getMeasuringTimeNano() : 100000L);
		if(pushTickTimeLog)
		{
			this.waitTime += Util.getMeasuringTimeNano() - beforeParkNanosMeasuringTimeNano;
		}
	}
	
	@Shadow
	private long tickStartTimeNanos;
	
	@Shadow
	public abstract void runTasks(final BooleanSupplier stopCondition);
	
	@Shadow
	protected abstract boolean shouldKeepTicking();
	
	@Shadow
	public abstract boolean shouldPushTickTimeLog();
	
	@Shadow
	private long waitTime;
}
