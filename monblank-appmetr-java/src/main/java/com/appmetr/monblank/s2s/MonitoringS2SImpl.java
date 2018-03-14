package com.appmetr.monblank.s2s;

import com.appmetr.monblank.*;
import com.appmetr.monblank.s2s.bridge.PersistenceStopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonitoringS2SImpl implements Monitoring {

    private final Map<MonitorKey, Counter> monitors = new ConcurrentHashMap<>();

    @Override public synchronized List<Counter> reset() {
        final List<Counter> counters = new ArrayList<>(monitors.values());
        monitors.clear();
        return counters;
    }

    @Override public StopWatch start(String group, String monitorName) {
        return create(new MonitorKey(buildMonitorName(group, monitorName, MonblankConst.MS))).start();
    }

    @Override public StopWatch start(String group, String monitorName, Map<String, String> properties) {
        return create(new MonitorKey(buildMonitorName(group, monitorName, MonblankConst.MS), properties)).start();
    }

    @Override public StopWatch start(MonitorKey key) {
        return create(key).start();
    }

    @Override public void add(String group, String monitorName, String units, double value) {
        updateCounter(new MonitorKey(buildMonitorName(group, monitorName, units)), value);
    }

    @Override
    public void add(String group, String monitorName, String units, double value, Map<String, String> properties) {
        updateCounter(new MonitorKey(buildMonitorName(group, monitorName, units), properties), value);
    }

    @Override public void add(MonitorKey key, double value) {
        updateCounter(key, value);
    }

    @Override public void set(String group, String monitorName, String units, double value, Map<String, String> properties) {
        setCounter(new MonitorKey(buildMonitorName(group, monitorName, units), properties), value);
    }

    @Override public void set(String group, String monitorName, String units, double value) {
        setCounter(new MonitorKey(buildMonitorName(group, monitorName, units)), value);
    }

    @Override public void set(MonitorKey key, double value) {
        setCounter(key, value);
    }

    @Override public void inc(String group, String monitorName) {
        updateCounter(new MonitorKey(buildMonitorName(group, monitorName, MonblankConst.COUNT)), 1.0);
    }

    @Override public void inc(String group, String monitorName, Map<String, String> properties) {
        updateCounter(new MonitorKey(buildMonitorName(group, monitorName, MonblankConst.COUNT), properties), 1.0);
    }

    @Override public void inc(MonitorKey key) {
        updateCounter(key, 1.0);
    }

    /**
     * Creates monitor and returns it instance
     *
     * @param key - key for monitor.<br/><b>Do not use character '@' in group or monitor name!</b>
     * @return - monitor instance to stop created monitor
     */
    private StopWatch create(MonitorKey key) {
        return new PersistenceStopWatch(this, key);
    }

    private void updateCounter(MonitorKey key, double value) {
        getOrCreateCounter(key).update(value);
    }

    private void setCounter(MonitorKey key, double value) {
        getOrCreateCounter(key).set(value);
    }

    private Counter getOrCreateCounter(MonitorKey key) {
        return monitors.computeIfAbsent(key, Counter::new);
    }

    private String buildMonitorName(String group, String monitorName, String units) {
        return group + MonblankConst.EVENT_DELIMITER + monitorName + wrapIfSet(units);
    }

    private static String wrapIfSet(String units) {
        if (units == null || units.isEmpty()) {
            return "";
        }
        return MonblankConst.UNIT_DELIMITER + "(" + units + ")";
    }
}
