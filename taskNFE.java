public class taskNFE extends Exception {
    public taskNFE(String message) {
        super(message);
    }
}

// a bit iffy about using this but making a custom exception class might be easier to do
// just throw taskNFE with the message