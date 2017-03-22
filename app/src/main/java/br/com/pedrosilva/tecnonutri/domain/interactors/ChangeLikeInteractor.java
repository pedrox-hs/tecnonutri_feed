package br.com.pedrosilva.tecnonutri.domain.interactors;

/**
 * Created by psilva on 3/21/17.
 */

public interface ChangeLikeInteractor {
    interface Callback {
        void onChange(String feedHash, boolean liked);
    }
}
