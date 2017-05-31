package pkgPreference;

/**
 * @author Marco Wilscher
 */

public class OptionEntity<ViewType, ValueType> {
    ViewType view;
    ValueType value;

    public OptionEntity(ViewType view, ValueType value) {
        this.view = view;
        this.value = value;
    }

    public OptionEntity () {

    }

    public void setValue (ValueType value) {
        this.value = value;
    }

    public ValueType getValue () {
        return value;
    }

    public void setView (ViewType view) {
        this.view = view;
    }

    public ViewType getView () {
        return view;
    }
}
