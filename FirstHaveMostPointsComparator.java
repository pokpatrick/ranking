package com.f4.owl.ranking;

import java.util.Comparator;

/**
 * @author ppk
 * @since 06/05/11 16:24
 */
public class FirstHaveMostPointsComparator
        implements Comparator<Player>
{
    @Override
    public int compare( final Player player1, final Player player2 )
    {
        final int ret = player2.getPlayerScore().compareTo( player1.getPlayerScore() );
        if( ret == 0 )
        {
            return ( player1.getPlayerId().compareTo( player2.getPlayerId() ) );
        }
        else
        {
            return ( ret );
        }
    }
}
