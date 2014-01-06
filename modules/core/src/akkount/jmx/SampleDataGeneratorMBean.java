/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedResource(description = "Sample data generator")
public interface SampleDataGeneratorMBean {

    @ManagedOperation(description = "Generates sample data")
    String generateSampleData(int numberOfDaysBack);

    @ManagedOperation(description = "Removes all data from the database. Pass 'ok' in parameter")
    String removeAllData(String confirm);
}
