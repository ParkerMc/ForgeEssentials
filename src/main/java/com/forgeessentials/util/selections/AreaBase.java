package com.forgeessentials.util.selections;

import com.forgeessentials.data.api.SaveableObject;
import com.forgeessentials.data.api.SaveableObject.SaveableField;

@SaveableObject
public class AreaBase {
	
    @SaveableField
    private Point high;
    
    @SaveableField
    private Point low;

    /**
     * Points are inclusive.
     *
     * @param p1
     * @param p2
     */
    public AreaBase(Point p1, Point p2)
    {
        low = getMinPoint(p1, p2);
        high = getMaxPoint(p1, p2);
    }

    public int getXLength()
    {
        return high.x - low.x + 1;
    }

    public int getYLength()
    {
        return high.y - low.y + 1;
    }

    public int getZLength()
    {
        return high.z - low.z + 1;
    }

    public Point getHighPoint()
    {
        return high;
    }

    public Point getLowPoint()
    {
        return low;
    }

    /**
     * Get the lowest XYZ coordinate in OOBB [p1,p2]
     */
    public static Point getMinPoint(Point p1, Point p2)
    {
    	return new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.min(p1.z, p2.z));
    }
    
    /**
     * Get the highest XYZ coordinate in OOBB [p1,p2]
     */
    public static Point getMaxPoint(Point p1, Point p2)
    {
    	return new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y), Math.max(p1.z, p2.z));
    }

    /**
     * Determines if a given point is within the bounds of an area.
     *
     * @param p Point to check against the Area
     * @return True, if the Point p is inside the area.
     */
    public boolean contains(Point p)
    {
        return (high.isGreaterThan(p) || high.equals(p)) && (low.isLessThan(p) || low.equals(p));
    }

    /**
     * checks if this area contains with another
     *
     * @param area to check against this area
     * @return True, AreaBAse area is completely within this area
     */
    public boolean contains(AreaBase area)
    {
        if (this.contains(area.high) && this.contains(area.low))
        {
            return true;
        }
        return false;
    }

    /**
     * checks if this area is overlapping with another
     *
     * @param area to check against this area
     * @return True, if the given area overlaps with this one.
     */
    public boolean intersectsWith(AreaBase area)
    {
        if (this.contains(area.high) || this.contains(area.low))
        {
            return true;
        }
        return false;
    }

    /**
     * @param area The area to be checked.
     * @return NULL if the areas to do not intersect. Argument if this area
     * completely contains the argument.
     */
    public AreaBase getIntersection(AreaBase area)
    {
        if (intersectsWith(area))
        {
            return null;
        }
        else if (this.contains(area))
        {
            return area;
        }
        else
        {
            return new Selection(getMaxPoint(low, area.low), getMinPoint(high, area.high));
        }
    }

    public boolean makesCuboidWith(AreaBase area)
    {
        boolean alignX = low.x == area.low.x && high.x == area.high.x;
        boolean alignY = low.y == area.low.y && high.y == area.high.y;
        boolean alignZ = low.z == area.low.z && high.z == area.high.z;

        return alignX || alignY || alignZ;
    }

    /**
     * @param area The area to be checked.
     * @return NULL if the areas to do not make a cuboid together.
     */
    public AreaBase getUnity(AreaBase area)
    {
        if (!makesCuboidWith(area))
        {
            return null;
        }
        else
        {
            return new Selection(getMinPoint(low, area.low), getMaxPoint(high, area.high));
        }
    }

    public void redefine(Point p1, Point p2)
    {
        low = getMinPoint(p1, p2);
        high = getMaxPoint(p1, p2);
    }

    public AreaBase copy()
    {
        return new Selection(low, high);
    }

    @Override
    public String toString()
    {
        return " {" + high.toString() + " , " + low.toString() + " }";
    }
}
