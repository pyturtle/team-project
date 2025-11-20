package data_access;

import entity.subgoal.Subgoal;
import entity.subgoal.SubgoalFactory;
import use_case.subgoal.show_subgoal.SubgoalDataAccessInterface;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple in-memory implementation of SubgoalDataAccessInterface.
 * <p>
 * This class stores Subgoal entities in a Map keyed by their ID.
 * It can be used as a placeholder until a file- or DB-backed DAO
 * is implemented by the team.
 */
public class FileSubgoalDataAccessObject implements SubgoalDataAccessInterface {

    private static final String HEADER = "id;planId;username;name;description;deadline;isCompleted;priority";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, Subgoal> subgoals = new HashMap<>();

    /**
     * Constructs an empty SubgoalDataAccessObject.
     */
    public FileSubgoalDataAccessObject(String csvPath, SubgoalFactory subgoalFactory) {
        csvFile = new File(csvPath);
        headers.put("id", 0);
        headers.put("planId", 1);
        headers.put("username", 2);
        headers.put("name", 3);
        headers.put("description", 4);
        headers.put("deadline", 5);
        headers.put("isCompleted", 6);
        headers.put("priority", 7);

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
                    final String planId = String.valueOf(col[headers.get("planId")]);
                    final String username = String.valueOf(col[headers.get("username")]);
                    final String name = String.valueOf(col[headers.get("name")]);
                    final String description = String.valueOf(col[headers.get("description")]);
                    final LocalDate deadline = LocalDate.parse(String.valueOf(col[headers.get("deadline")]));
                    final boolean isCompleted = Boolean.valueOf(String.valueOf(col[headers.get("isCompleted")]));
                    final boolean priority = Boolean.valueOf(String.valueOf(col[headers.get("priority")]));
                    final Subgoal subgoal = subgoalFactory.create(id,
                            planId,
                            username,
                            name,
                            description,
                            deadline,
                            isCompleted,
                            priority);
                    subgoals.put(id, subgoal);
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

            for (Subgoal subgoal : subgoals.values()) {
                final String line = String.format("%s;%s;%s;%s;%s;%s;%s;%s",
                        subgoal.getId(),
                        subgoal.getPlanId(),
                        subgoal.getUsername(),
                        subgoal.getName(),
                        subgoal.getDescription(),
                        subgoal.getDeadline(),
                        subgoal.isCompleted(),
                        subgoal.isPriority());
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    /**
     * Adds or replaces a subgoal in the in-memory map.
     * This is mainly useful for seeding data in AppBuilder.
     *
     * @param subgoal the subgoal to store
     */
    public void save(Subgoal subgoal) {
        subgoals.put(subgoal.getId(), subgoal);
        this.save();
    }

    @Override
    public Subgoal getSubgoalById(String id) {
        return subgoals.get(id);
    }

    @Override
    public void updatePriority(String id, boolean priority) {
        Subgoal old = subgoals.get(id);
        if (old == null) {
            return;
        }
        // Subgoal is immutable, so we create a new one with the updated priority.
        Subgoal updated = new Subgoal(
                old.getId(),
                old.getPlanId(),
                old.getUsername(),
                old.getName(),
                old.getDescription(),
                old.getDeadline(),
                old.isCompleted(),
                priority
        );
        subgoals.put(id, updated);
        this.save();
    }

    @Override
    public void updateCompleted(String id, boolean completed) {
        Subgoal old = subgoals.get(id);
        if (old == null) {
            return;
        }

        Subgoal updated = new Subgoal(
                old.getId(),
                old.getPlanId(),
                old.getUsername(),
                old.getName(),
                old.getDescription(),
                old.getDeadline(),
                completed,
                old.isPriority()
        );

        subgoals.put(id, updated);
        this.save();
    }

}
