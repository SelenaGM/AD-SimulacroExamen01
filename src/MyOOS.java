import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyOOS extends ObjectOutputStream {
    public MyOOS(OutputStream out) throws IOException {
        super(out);
    }


    @Override
    protected void writeStreamHeader() throws IOException {

    }
}
