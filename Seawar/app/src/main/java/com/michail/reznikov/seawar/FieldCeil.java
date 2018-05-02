package com.michail.reznikov.seawar;

public class FieldCeil {
    public boolean is_free;
    public boolean is_shooted;
    FieldCeil()
    {
        is_free=true;
        is_shooted=false;
    }
    public FieldCeil clone()
    {
        FieldCeil fieldceil = new FieldCeil();
        fieldceil.is_free=this.is_free;
        fieldceil.is_shooted=this.is_shooted;
        return fieldceil;
    }
}
