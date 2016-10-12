package org.apache.commons.collections4.map;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.map.PassiveExpiringMap.ExpirationPolicy;

import com.google.common.collect.Lists;

public class ListenableExpiringMap<K, V> extends AbstractExpiringMap<K, V>
{

	/**
	 * implement this interface to get notifications about evictions of elements due to expiration
	 */
	@FunctionalInterface
	public interface IExpirationListener<K, V>
	{
		/**
		 * will ve called AFTER the key-value pair expired and removed from the Map
		 */
		public void onExpired(K key, V value);
	}

	private static final long serialVersionUID = 1L;

	public ListenableExpiringMap()
	{
		super();
	}

	public ListenableExpiringMap(ExpirationPolicy<K, V> expiringPolicy, Map<K, V> map)
	{
		super(expiringPolicy, map);
	}

	public ListenableExpiringMap(ExpirationPolicy<K, V> expiringPolicy)
	{
		super(expiringPolicy);
	}

	public ListenableExpiringMap(long timeToLiveMillis, Map<K, V> map)
	{
		super(timeToLiveMillis, map);
	}

	public ListenableExpiringMap(long timeToLive, TimeUnit timeUnit, Map<K, V> map)
	{
		super(timeToLive, timeUnit, map);
	}

	public ListenableExpiringMap(long timeToLive, TimeUnit timeUnit)
	{
		super(timeToLive, timeUnit);
	}

	public ListenableExpiringMap(long timeToLiveMillis)
	{
		super(timeToLiveMillis);
	}

	public ListenableExpiringMap(Map<K, V> map)
	{
		super(map);
	}

	protected final transient List<IExpirationListener<K, V>> expirationListeners = Lists.newArrayList();

	@Override
	@SuppressWarnings("unchecked")
	protected void notifyExpired(Object key, V value)
	{
		for (IExpirationListener<K, V> el : expirationListeners)
		{
			el.onExpired((K) key, value);
		}
	}

	public void addExpirationListener(IExpirationListener<K, V> listener)
	{
		if (listener != null)
		{
			expirationListeners.add(listener);
		}
	}

	public boolean removeExpirationListener(IExpirationListener<K, V> listener)
	{
		return expirationListeners.remove(listener);
	}

	@SuppressWarnings("unchecked")
	public IExpirationListener<K, V>[] getListeners()
	{
		return expirationListeners.toArray(new IExpirationListener[expirationListeners.size()]);
	}

}
