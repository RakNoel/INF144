import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.fail;

/**
 * TODO: Describe test
 *
 * @author RakNoel
 * @version 1.0
 * @since 25.03.18
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HuffmanTest {

    @Test
    public void TreeOutput() {
        //TODO: Implement tests
        fail("Test not yet implemented");
    }

    @Test
    public void Compress_test() {

        String laphabethText = "abbccc";
        System.out.println(Huffman.compressText(laphabethText));
    }
}