class UseThreadLocal {
    public static ThreadLocal<Integer> makeThreadLocal(int counter) {
        // write your code here
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        threadLocal.set(counter + 1);
        return threadLocal;
    }
}