/**
 * Класс обертка
 * @author Denis Popov
 * @version 1.0
 */
public class Wrapper<E> {
        E ref;
        public Wrapper(E e ){
            ref = e;
        }
        public E get() { return ref; }
        public void set( E e ){ this.ref = e; }

        public String toString() {
            return ref.toString();
        }


}
