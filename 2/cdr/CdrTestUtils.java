public class CdrTestUtils {
    
    public static File createTempCdrFile(List<String> records) throws IOException {
        File tempFile = File.createTempFile("test-cdr-", ".csv");
        Files.write(tempFile.toPath(), records, StandardCharsets.UTF_8);
        return tempFile;
    }
}