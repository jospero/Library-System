package model;

import impresario.IModel;
import impresario.IView;

/**
 * Created by Sammytech on 2/28/17.
 */
public class Library implements IView, IModel {
    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    public Object getState(String key) {
        return null;
    }

    @Override
    public void subscribe(String key, IView subscriber) {

    }

    @Override
    public void unSubscribe(String key, IView subscriber) {

    }

    @Override
    public void stateChangeRequest(String key, Object value) {

    }
}
