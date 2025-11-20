package data_access;

import entity.plan.Plan;
import entity.plan.PlanFactory;
import entity.user.User;
import use_case.plan.PlanDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FilePlanDataAccessObject implements PlanDataAccessInterface {

    private static final String HEADER = "id;name;description;username";
    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, Plan> plans = new HashMap<>();

    public FilePlanDataAccessObject(String csvPath, PlanFactory planFactory) {
        this.csvFile = new File(csvPath);
        headers.put("id", 0);
        headers.put("name", 1);
        headers.put("description", 2);
        headers.put("username", 3);
        if (csvFile.length() == 0) {
            save();
        }
        else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(";");
                    final String id = String.valueOf(col[headers.get("id")]);
                    final String name = String.valueOf(col[headers.get("name")]);
                    final String description = String.valueOf(col[headers.get("description")]);
                    final String username = String.valueOf(col[headers.get("username")]);
                    final Plan plan = planFactory.create(id, name, description, username);
                    plans.put(id, plan);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(";", headers.keySet()));
            writer.newLine();

            for (Plan plan : plans.values()) {
                final String line = String.format("%s;%s;%s;%s",
                        plan.getId(),
                        plan.getName(),
                        plan.getDescription(),
                        plan.getUsername());
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(Plan plan) {
        plans.put(plan.getId(), plan);
        this.save();
    }

    @Override
    public String getPlanNameById(String planId) {
        return plans.get(planId).getName();
    }

    @Override
    public String getPlanDescriptionById(String planId) {
        return plans.get(planId).getDescription();
    }
}
