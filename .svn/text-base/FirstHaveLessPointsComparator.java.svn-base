package com.f4.owl.ranking;

import java.util.Comparator;

/**
 * @author ppk
 * @since 06/05/11 16:23
 */
public class FirstHaveLessPointsComparator
        implements Comparator<Player>
{
    @Override
    public int compare( final Player player1, final Player player2 )
    {
        final int ret = player1.getPlayerScore().compareTo( player2.getPlayerScore() );
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
