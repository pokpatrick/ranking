package com.f4.owl.ranking;

import java.util.Date;

/**
 * @author ppk
 * @since 27/06/11 16:38
 */
public class ClearDataDate
{
    public ClearDataDate()
    {
    }

    public ClearDataDate( final int dateId, final Date date )
    {
        _dateId = dateId;
        _date = date;
    }

    @Override
    public String toString()
    {
        return ( "Unique Id : " + _dateId + "; Date : " + _date.toString() );
    }

    @Override
    public int hashCode()
    {
        int result = _dateId;
        result = 31 * result + ( _date != null ? _date.hashCode() : 0 );
        return result;
    }

    public int getDateId()
    {
        return _dateId;
    }

    public void setDateId( final int dateId )
    {
        _dateId = dateId;
    }

    public Date getDate()
    {
        return _date;
    }

    public void setDate( final Date date )
    {
        _date = date;
    }

    private int _dateId;
    private Date _date;
}
