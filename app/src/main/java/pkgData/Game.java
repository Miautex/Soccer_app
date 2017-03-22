package pkgData;

import android.support.annotation.NonNull;

import java.io.Serializable;

public final class Game implements Serializable, Comparable<Game> {

    public Game () {
    }

    @Override
    public int hashCode () {
        return super.hashCode();
    }

    @Override
    public boolean equals (Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString () {
        return super.toString();
    }

    @Override
    protected void finalize () throws Throwable {
        super.finalize();
    }

    @Override
    public int compareTo (@NonNull Game game) {
        return 0;
    }
}
