package akkount.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Sample data generator")
public interface SampleDataGeneratorMBean {

    @ManagedOperation(description = "Generates sample data. Enter a number of days.")
    String generateSampleData(int numberOfDaysBack);

    @ManagedOperation(description = "Removes all data from the database. Pass 'ok' in parameter")
    String removeAllData(String confirm);
}
