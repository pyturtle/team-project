package app;

import data_access.InMemorySubgoalDataAccessObject;
import data_access.SubgoalDataAccessInterface;
import entity.Subgoal;
import use_case.filter_subgoals.*;

import java.time.LocalDate;
import java.util.List;

public class FilterSubgoalsTest {

    public static void main(String[] args) {

        // 1. Create fake DAO
        SubgoalDataAccessInterface dao = new InMemorySubgoalDataAccessObject();

        // 2. Add sample subgoals
        ((InMemorySubgoalDataAccessObject) dao).add(new Subgoal(
                1, 10, 99, "Goal A", "Desc A",
                LocalDate.now(), false, false
        ));

        ((InMemorySubgoalDataAccessObject) dao).add(new Subgoal(
                2, 10, 99, "Goal B", "Desc B",
                LocalDate.now(), false, true
        ));

        ((InMemorySubgoalDataAccessObject) dao).add(new Subgoal(
                3, 20, 99, "Goal C", "Desc C",
                LocalDate.now(), false, true
        ));

        // 3. Fake presenter to print results
        FilterSubgoalsOutputBoundary presenter = output -> {
            System.out.println("=== FILTER RESULT ===");
            List<Subgoal> list = output.getFilteredSubgoals();
            for (Subgoal s : list) {
                System.out.println(s.getId() + ": " + s.getName());
            }
        };

        // 4. Create interactor
        FilterSubgoalsInputBoundary interactor =
                new FilterSubgoalsInteractor(dao, presenter);

        // 5. Run tests
        System.out.println("\nTest 1: All subgoals for user 99");
        interactor.filter(new FilterSubgoalsInputData(99, null, false));

        System.out.println("\nTest 2: Filter by planId = 10");
        interactor.filter(new FilterSubgoalsInputData(99, 10, false));

        System.out.println("\nTest 3: Priority only");
        interactor.filter(new FilterSubgoalsInputData(99, null, true));
    }
}
