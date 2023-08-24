/**
 * This code has been kindly borrowed from bergerkiller's BKCommonLib
 *
 * MIT License
 *
 * Copyright (C) 2013-2015 bergerkiller Copyright (C) 2016-2020 Berger Healer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, and/or sublicense the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.jverbruggen.jrides.models.math;

import com.comphenix.protocol.wrappers.Vector3F;
import com.jverbruggen.jrides.api.JRidesPlayerLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;

public class Vector3 {
    private static final Vector3 ZERO = new Vector3(0,0,0);

    public double x;
    public double y;
    public double z;

    public Vector3() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public Vector3(Vector vector) {
        this(vector.getX(), vector.getY(), vector.getZ());
    }

    public Vector3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 zero() {
        return Vector3.ZERO;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public int getBlockX(){
        return (int) x;
    }

    public int getBlockY(){
        return (int) y;
    }

    public int getBlockZ(){
        return (int) z;
    }

    public boolean isZero(){
        return x == 0 && y == 0 && z == 0;
    }

    public Vector2 getXY() {
        return new Vector2(x, y);
    }

    public Vector2 getXZ() {
        return new Vector2(x, z);
    }

    /**
     * Returns a new vector with the x/y/z values inverted.
     *
     * @return negated vector
     */
    public Vector3 negate() {
        return new Vector3(-x, -y, -z);
    }

    public double length(){
        return Math.sqrt(x*x+y*y+z*z);
    }

    /**
     * Returns a new normalized vector of this vector (length = 1)
     *
     * @return normalized vector
     */
    public Vector3 normalize() {
        double len = this.length();
        return new Vector3((x/len), (y/len), (z/len));
    }

    public double distanceSquared(Vector3 v) {
        double dx = (this.x - v.x);
        double dy = (this.y - v.y);
        double dz = (this.z - v.z);
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Vector3 clone() {
        return new Vector3(x, y, z);
    }

    @Override
    public String toString() {
        return "{x=" + x + ", y=" + y + ", z=" + z + "}";
    }

    public String toShortString(){
        return "{" + ((double)(int)(this.x*10))/10 + ", " + ((double)(int)(this.y*10))/10 + ", " + ((double)(int)(this.z*10))/10 + "}";
    }

    public Vector toBukkitVector(){
        return new Vector(x, y, z);
    }

    public Location toBukkitLocation(World world) {
        return new Location(world, x, y, z);
    }

    public Location toBukkitLocation(World world, double yaw) {
        return new Location(world, x, y, z, (float)yaw, 0f);
    }

    public Vector3F toVector3F(){
        float x = (float) this.x;
        float y = (float) this.y;
        float z = (float) this.z;
        return new Vector3F(x, y, z);
    }

    public Vector3f toVector3fJoml(){
        return new Vector3f((float) x, (float) y, (float) z);
    }

    public Vector3 toBlock(){
        int blockX = this.getBlockX();
        int blockY = this.getBlockY();
        int blockZ = this.getBlockZ();
        return new Vector3(blockX, blockY, blockZ);
    }

    public boolean equals(Vector3 p) {
        return p.x == x && p.y == y && p.z == z;
    }

    public static Vector3 multiply(Vector3 v, double multiplication){
        return new Vector3(
                v.x * multiplication,
                v.y * multiplication,
                v.z * multiplication
        );
    }

    /**
     * Returns the cross product of two vectors
     *
     * @param v1 first vector
     * @param v2 second vector
     * @return cross product
     */
    public static Vector3 cross(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.y*v2.z - v1.z*v2.y,
                v2.x*v1.z - v2.z*v1.x,
                v1.x*v2.y - v1.y*v2.x
        );
    }

    /**
     * Returns the vector subtraction of two vectors (v1 - v2)
     *
     * @param v1
     * @param v2
     * @return subtracted vector
     */
    public static Vector3 subtract(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.x - v2.x,
                v1.y - v2.y,
                v1.z - v2.z
        );
    }

    public static Vector3 add(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.x + v2.x,
                v1.y + v2.y,
                v1.z + v2.z
        );
    }

    /**
     * Returns the vector average of two vectors ((v1 + v2) / 2)
     *
     * @param v1
     * @param v2
     * @return average vector
     */
    public static Vector3 average(Vector3 v1, Vector3 v2) {
        return new Vector3(
                (v1.x + v2.x) / 2.0f,
                (v1.y + v2.y) / 2.0f,
                (v1.z + v2.z) / 2.0f
        );
    }

    /**
     * Returns the vector average of three vectors ((v1 + v2 + v3) / 3)
     *
     * @param v1
     * @param v2
     * @param v3
     * @return average vector
     */
    public static Vector3 average(Vector3 v1, Vector3 v2, Vector3 v3) {
        return new Vector3(
                (v1.x + v2.x + v3.x) / 3.0f,
                (v1.y + v2.y + v3.y) / 3.0f,
                (v1.z + v2.z + v3.z) / 3.0f
        );
    }

    /**
     * Returns the vector average of four vectors ((v1 + v2 + v3 + v4) / 4)
     *
     * @param v1
     * @param v2
     * @param v3
     * @param v4
     * @return average vector
     */
    public static Vector3 average(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 v4) {
        return new Vector3(
                (v1.x + v2.x + v3.x + v4.x) / 4.0f,
                (v1.y + v2.y + v3.y + v4.y) / 4.0f,
                (v1.z + v2.z + v3.z + v4.z) / 4.0f
        );
    }

    /**
     * Returns the vector dot product of two vectors (v1 . v2)
     *
     * @param v1
     * @param v2
     * @return vector dot product
     */
    public static double dot(Vector3 v1, Vector3 v2) {
        return (v1.x*v2.x + v1.y*v2.y + v1.z*v2.z);
    }

    public static Vector3 fromDoubleList(List<Double> doubleList){
        if(doubleList == null) return null;
        if(doubleList.size() != 3) throw new RuntimeException("Can only create vector from double list of length 3");
        return new Vector3(doubleList.get(0), doubleList.get(1), doubleList.get(2));
    }

    public static Vector3 fromBukkitLocation(@Nullable Location bukkitLocation) {
        if(bukkitLocation == null) return null;
        return new Vector3(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ());
    }

    public static boolean chunkRotated(Vector3 v1, Vector3 v2, int chunkSize){
        boolean xModRotated = (int)(v2.getX() / chunkSize) != (int)(v1.getX() / chunkSize);
        boolean yModRotated = (int)(v2.getY() / chunkSize) != (int)(v1.getY() / chunkSize);
        boolean zModRotated = (int)(v2.getZ() / chunkSize) != (int)(v1.getZ() / chunkSize);

        return xModRotated || yModRotated || zModRotated;
    }

    public static boolean chunkRotated(Location l1, Location l2, int chunkSize){
        boolean xModRotated = (int)(l2.getX() / chunkSize) != (int)(l1.getX() / chunkSize);
        boolean yModRotated = (int)(l2.getY() / chunkSize) != (int)(l1.getY() / chunkSize);
        boolean zModRotated = (int)(l2.getZ() / chunkSize) != (int)(l1.getZ() / chunkSize);

        return xModRotated || yModRotated || zModRotated;
    }

    public static Vector3 fromPlayerLocation(JRidesPlayerLocation playerLocation){
        return new Vector3(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ());
    }

}