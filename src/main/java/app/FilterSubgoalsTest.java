package app;

import data_access.InMemorySubgoalDataAccessObject;
import entity.subgoal.Subgoal;
import use_case.filter_subgoals.*;
import java.time.LocalDate;
import java.util.List;
import use_case.subgoal.show_subgoal.SubgoalDataAccessInterface;


public class FilterSubgoalsTest {

    public static void main(String[] args) {

        // 1. Create fake DAO
        use_case.subgoal.show_subgoal.SubgoalDataAccessInterface dao = new InMemorySubgoalDataAccessObject();

        // 2. Add sample subgoals - FIXED: Use String IDs
        ((InMemorySubgoalDataAccessObject) dao).add(new Subgoal(
                "1", "10", "99", "Goal A", "Desc A",  // All IDs as Strings
                LocalDate.now(), false, false
        ));

        ((InMemorySubgoalDataAccessObject) dao).add(new Subgoal(
                "2", "10", "99", "Goal B", "Desc B",  // All IDs as Strings
                LocalDate.now(), false, true
        ));

        ((InMemorySubgoalDataAccessObject) dao).add(new Subgoal(
                "3", "20", "99", "Goal C", "Desc C",  // All IDs as Strings
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

        // 5. Run tests - FIXED: Use String parameters
        System.out.println("\nTest 1: All subgoals for user 99");
        interactor.filter(new FilterSubgoalsInputData("99", null, false));  // "99" not 99

        System.out.println("\nTest 2: Filter by planId = 10");
        interactor.filter(new FilterSubgoalsInputData("99", "10", false));  // "10" not 10

        System.out.println("\nTest 3: Priority only");
        interactor.filter(new FilterSubgoalsInputData("99", null, true));   // "99" not 99
    }
}