package com.github.rfoltyns.stats;

import java.io.IOException;

public interface StatsListener {

    void notify(Percentiles snapshot) throws IOException;

    String getTarget();
}
