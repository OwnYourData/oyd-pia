package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.Application;
import eu.ownyourdata.pia.domain.Data;
import eu.ownyourdata.pia.domain.DatatypeCount;
import eu.ownyourdata.pia.domain.Datatype;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DatatypeRepositoryTest {

    @Inject
    private DataRepository dataRepository;

    @Inject
    private DatatypeRepository datatypeRepository;

    private Datatype datatypeA;
    private Datatype datatypeB;

    @Before
    public void init() {
        datatypeRepository.deleteAll();

        datatypeA = createDatatype("datatype: a");
        datatypeB = createDatatype("datatype: b");

        createData(datatypeA,5);
        createData(datatypeB,2);
    }

    @Test
    @Ignore
    public void testGetCounts() throws Exception {
        List<DatatypeCount> counts = datatypeRepository.getCounts();

        counts.forEach(x -> {
            if (datatypeA.getName().equals(x.getType())) {
                assertThat(x.getCount(),is(5L));
            }
            if (datatypeB.getName().equals(x.getType())) {
                assertThat(x.getCount(),is(2L));
            }
        });
    }

    private Datatype createDatatype(String name) {
        Datatype newDatatype = new Datatype();
        newDatatype.setName(name);

        return datatypeRepository.save(newDatatype);
    }

    private void createData(Datatype datatype, int amount) {
        for(int i=0;i<amount;i++) {
            Data newData = new Data();
            newData.setType(datatype);
            newData.setValue("Value"+i);
            dataRepository.save(newData);
        }
    }
}
