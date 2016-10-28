package org.saiku.service.olap.totals;

import org.olap4j.CellSetAxis;
import org.olap4j.Position;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;
import org.olap4j.metadata.Property;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class AxisInfo {
  public List<Integer>[] levels;
  public final List<String> uniqueLevelNames = new ArrayList<>();
  public int maxDepth;
  public int measuresMember;
  public List<Position> fullPositions;
  public final CellSetAxis axis;

  public AxisInfo( CellSetAxis axis ) {
    this.axis = axis;
    calcAxisInfo( axis );
  }

  private void calcAxisInfo( CellSetAxis axis ) {
    calcAxisInfo( this, axis );
  }

  private static void calcAxisInfo( AxisInfo axisInfo, CellSetAxis axis ) {
    final List<Hierarchy> hierarchies = axis.getAxisMetaData().getHierarchies();
    final int hCount = hierarchies.size();
    final List<Integer> levels[] = new List[ hCount ];
    final HashSet<Integer>[][] usedLevels = new HashSet[ hCount ][];
    final int[] maxDepth = new int[ hCount ];

    for ( int i = 0; i < hCount; i++ ) {
      maxDepth[ i ] = -1;
      levels[ i ] = new ArrayList<>();


        usedLevels[i] = new HashSet[hierarchies.get(i).getLevels().size()];

      for ( int j = 0; j < usedLevels[ i ].length; j++ ) {
        usedLevels[ i ][ j ] = new HashSet<>();
      }
    }
    axisInfo.measuresMember = Integer.MIN_VALUE;

    for ( final Position p : axis.getPositions() ) {
      int mI = 0;
      for ( final Member m : p.getMembers() ) {
        if ( "Measures".equals( m.getDimension().getName() ) ) {
          axisInfo.measuresMember = mI;
        }
        usedLevels[ mI ][ m.getLevel().getDepth() ].add( m.getDepth() );
        mI++;
      }
    }

    for ( int i = 0; i < usedLevels.length; i++ ) {
      for ( int j = 0; j < usedLevels[ i ].length; j++ ) {
        if ( usedLevels[ i ][ j ].size() > 0 ) {
          HashSet<Integer> obj = usedLevels[i][j];
          Iterator<Integer> it = obj.iterator();
          while(it.hasNext()){
            levels[ i ].add( it.next() );
          }


          axisInfo.uniqueLevelNames.add( hierarchies.get( i ).getLevels().get( j ).getUniqueName() );
        }
      }
    }

    int maxAxisDepth = 0;
    for ( int i = 0; i < hCount; i++ ) {
      maxAxisDepth += levels[ i ].size();
    }
    axisInfo.levels = levels;
    axisInfo.maxDepth = maxAxisDepth;
    findFullPositions( axisInfo, axis );
  }




  private static void findFullPositions( AxisInfo axisInfo, CellSetAxis axis ) {
    axisInfo.fullPositions = new ArrayList<>(axis.getPositionCount());
    List<Integer>[] levels = axisInfo.levels;
    nextpos:
    for ( final Position p : axis.getPositions() ) {
      int mI = 0;
      for ( final Member m : p.getMembers() ) {
        final int maxDepth = levels[ mI ].get( levels[ mI ].size() - 1 );
        if ( m.getDepth() < maxDepth ) {
          continue nextpos;
        }
        mI++;
      }
      axisInfo.fullPositions.add( p );
    }
  }
}