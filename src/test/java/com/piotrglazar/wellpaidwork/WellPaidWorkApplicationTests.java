package com.piotrglazar.wellpaidwork;

import com.google.common.collect.ImmutableSet;
import com.piotrglazar.wellpaidwork.model.Category;
import com.piotrglazar.wellpaidwork.model.Currency;
import com.piotrglazar.wellpaidwork.model.EmploymentType;
import com.piotrglazar.wellpaidwork.model.JobOffer;
import com.piotrglazar.wellpaidwork.model.Period;
import com.piotrglazar.wellpaidwork.model.Position;
import com.piotrglazar.wellpaidwork.model.Salary;
import com.piotrglazar.wellpaidwork.model.dao.JobOfferDao;
import com.piotrglazar.wellpaidwork.model.db.JobOfferSource;
import com.piotrglazar.wellpaidwork.service.JobMigrator;
import com.piotrglazar.wellpaidwork.service.SalaryConversionService;
import com.piotrglazar.wellpaidwork.util.Try;
import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class WellPaidWorkApplicationTests implements TestCreators {

    @Value("${sense.of.the.universe}")
    private int magicValue = 0;

    @Autowired
    private JobOfferDao dao;

    @Autowired
    private SalaryConversionService salaryConversionService;

    @Autowired
    private JobMigrator jobMigrator;

    @Test
    public void shouldLoadContext() {
        // given context loaded

        // expect
        assertThat(magicValue).isEqualTo(42);
    }

    @Test
    public void shouldSaveJobOffer() {
        // given
        JobOffer jobOffer = jobOffer("externalId");

        // when
        Try<Long> idTry = dao.save(jobOffer);

        // then
        Assertions.assertThat(idTry.isSuccess()).isTrue();
        Long id = idTry.get();
        Assertions.assertThat(id).isNotNull().isGreaterThan(0);
    }

    @Test
    public void shouldReadJobOffer() {
        // given
        JobOffer jobOffer = new JobOffer("fancy", "name", "city", Category.BACKEND, "senior developer",
                ImmutableSet.of("senior", "developer"), Position.DEVELOPER, new Salary(15000,
                18000, Period.MONTH, Currency.PLN), EmploymentType.PERMANENT,
                DateTime.parse("2017-01-01"), false, ImmutableSet.of("scala", "machine learning"),
                JobOfferSource.TEST, DateTime.parse("2017-01-01"),
                Optional.of(new Salary(180000, 216000, Period.YEAR, Currency.PLN)));
        Long id = dao.save(jobOffer).get();

        // when
        Optional<JobOffer> foundOffer = dao.find("fancy", JobOfferSource.TEST);

        // then
        assertThat(foundOffer)
                .isPresent()
                .hasValue(jobOffer.withId(id));
    }

    @Test
    public void shouldNotAllowJobOffersWithTheSameExternalIdAndSource() {
        // given
        JobOffer jobOffer = jobOffer("fancy");
        assertThat(dao.save(jobOffer).isSuccess());

        // when
        Try<Long> secondSave = dao.save(jobOffer);

        // then
        assertThat(secondSave.isFailure()).isTrue();
    }

    @Test
    public void shouldConvertYearlySalaryToMonthlySalary() {
        // given
        Salary salary = new Salary(65000, Period.YEAR, Currency.EUR);

        // when
        Optional<Salary> convertedSalary = salaryConversionService.convertSalary(salary);

        // then
        assertThat(convertedSalary)
                .isPresent()
                .hasValueSatisfying(s -> assertThat(s.getPeriod()).isEqualTo(Period.MONTH));
    }

    @Test
    public void shouldConvertDailySalaryToMonthlySalary() {
        // given
        Salary salary = new Salary(1000, Period.DAY, Currency.PLN);

        // when
        Optional<Salary> convertedSalary = salaryConversionService.convertSalary(salary);

        // then
        assertThat(convertedSalary)
                .isPresent()
                .hasValueSatisfying(s -> assertThat(s.getPeriod()).isEqualTo(Period.MONTH));
    }

    @Test
    public void shouldNotConvertAlreadyMonthlySalary() {
        // given
        Salary salary = new Salary(15000, Period.MONTH, Currency.PLN);

        // when
        Optional<Salary> convertedSalary = salaryConversionService.convertSalary(salary);

        // then
        assertThat(convertedSalary)
                .isEmpty();
    }

    @Test
    public void shouldMigrateEntryWhichHasUnconvertedSalaryPeriod() {
        // given
        JobOffer jobOffer = jobOffer("migratePeriod", new Salary(144000,
                216000, Period.YEAR, Currency.PLN));
        assertThat(dao.save(jobOffer).isSuccess());

        // when
        List<JobOffer> migrated = jobMigrator.migrate();

        // then
        assertThat(migrated).hasSize(1);
        assertThat(migrated.get(0)).extracting(jo -> jo.getSalary().getPeriod()).containsOnly(Period.MONTH);
        assertThat(dao.find("migratePeriod", JobOfferSource.TEST))
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o.getSalary()).isEqualTo(new Salary(12000, 18000, Period.MONTH, Currency.PLN)))
                .hasValueSatisfying(o -> assertThat(o.getOriginalSalary()).isPresent().hasValue(jobOffer.getSalary()));
    }

    @Test
    public void shouldMigrateEntryWhichHasUnconvertedSalaryCurrency() {
        // given
        JobOffer jobOffer = jobOffer("migrateCurrency", new Salary(2000,
                4000, Period.MONTH, Currency.EUR));
        assertThat(dao.save(jobOffer).isSuccess());

        // when
        List<JobOffer> migrated = jobMigrator.migrate();

        // then
        assertThat(migrated).hasSize(1);
        assertThat(migrated.get(0)).extracting(jo -> jo.getSalary().getPeriod()).containsOnly(Period.MONTH);
        assertThat(dao.find("migrateCurrency", JobOfferSource.TEST))
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o.getSalary()).isEqualTo(new Salary(8740, 17480, Period.MONTH, Currency.PLN)))
                .hasValueSatisfying(o -> assertThat(o.getOriginalSalary()).isPresent().hasValue(jobOffer.getSalary()));
    }

    @Test
    public void shouldMigrateEntryWhichHasUnconvertedSalaryCurrencyAndPeriod() {
        // given
        JobOffer jobOffer = jobOffer("migrateBoth", new Salary(45000,
                65000, Period.YEAR, Currency.EUR));
        assertThat(dao.save(jobOffer).isSuccess());

        // when
        List<JobOffer> migrated = jobMigrator.migrate();

        // then
        assertThat(migrated).hasSize(1);
        assertThat(migrated.get(0)).extracting(jo -> jo.getSalary().getPeriod()).containsOnly(Period.MONTH);
        assertThat(dao.find("migrateBoth", JobOfferSource.TEST))
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o.getSalary()).isEqualTo(new Salary(16388, 23672, Period.MONTH, Currency.PLN)))
                .hasValueSatisfying(o -> assertThat(o.getOriginalSalary()).isPresent().hasValue(jobOffer.getSalary()));
    }
}
