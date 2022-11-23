
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

public class TestContext extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @Test
  public void contextUp() { assertNotNull(entityManager());
  }

  @Test
  public void contextUpWithTransaction() throws Exception {
    withTransaction(() -> {});
  }

}
