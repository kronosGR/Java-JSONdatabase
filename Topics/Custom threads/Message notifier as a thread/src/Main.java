class MessageNotifier extends Thread {

    // write fields to store variables here
    int repeats;

    public MessageNotifier(String msg, int repeats) {
        // implement the constructor
        super(msg);
        this.repeats = repeats;
    }

    @Override
    public void run() {
        // implement the method to print the message stored in a field
        for (int i = 0; i < repeats; i++) {
            System.out.println(getName());

        }
    }
}