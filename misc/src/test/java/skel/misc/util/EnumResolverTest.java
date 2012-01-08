package skel.misc.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alexey Dudarev
 */
public class EnumResolverTest {

    private static enum TestEnum {
        FIRST,
        SECOND,
        THIRD;

        public static EnumResolver<TestEnum> R = EnumResolver.er(TestEnum.class);
    }

    @Test
    public void testResolve() {
        Assert.assertTrue(TestEnum.R.resolve(0) == TestEnum.FIRST);
        Assert.assertTrue(TestEnum.R.resolve(1) == TestEnum.SECOND);
        Assert.assertTrue(TestEnum.R.resolve(2) == TestEnum.THIRD);

        try {
            TestEnum.R.resolve(3);
            Assert.fail();
        } catch (Exception e) {
        }
    }
}
