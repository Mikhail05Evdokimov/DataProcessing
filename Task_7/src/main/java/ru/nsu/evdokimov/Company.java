package ru.nsu.evdokimov;

import java.util.ArrayList;
import java.util.List;
public final class Company {
    private final List<Department> departments;
    public Company(final int departmentsCount) {
        this.departments = new ArrayList<>(departmentsCount);
        int j = 3;
        int sign = -1;
        for (int i = 0; i < departmentsCount; i++) {
            departments.add(i, new Department(j*sign, departmentsCount));
            j += 2;
            sign *= -1;
        }
    }

    /**
     * Вывод результата по всем отделам.
     * P.S. Актуально после того, как все отделы выполнят свою работу.
     */
    public void showCollaborativeResult() {
        System.out.println("All departments have completed their work.");
        final double result = departments.stream()
            .map(Department::getCalculationResult)
            .reduce(Double::sum)
            .orElse(-1.0);
        System.out.println("The sum of all calculations is: " + (1 + result) * 4);
    }
    /**
     * @return Количество доступных отделов для симуляции выполнения
    работы.
     */
    public int getDepartmentsCount() {
        return departments.size();
    }
    /**
     * @param index Index для текущего свободного отдела.
     * @return Свободный отдел для симуляции выполнения работы.
     */
    public Department getFreeDepartment(final int index) {
        return departments.get(index);
    }
}