package mainEngine.core.Entities;

import mainEngine.core.utils.Consts;
import org.joml.Vector4f;

public class Material {
    private Vector4f ambientColour, diffuseColour, specularColor;
    private float reflectance;
    private Texture texture;

    public Material() {
        this.ambientColour = Consts.DEFAULT_COLOUR;
        this.diffuseColour = Consts.DEFAULT_COLOUR;
        this.specularColor = Consts.DEFAULT_COLOUR;
        this.texture = null;
        this.reflectance = 0;
    }

    public Material(Vector4f colour, float reflectance) {
        this(colour, colour, colour, reflectance, null);
    }

    public Material(Vector4f colour, float reflectance, Texture texture) {
        this(colour, colour, colour, reflectance, texture);
    }

    public Material(Texture texture) {
        this(Consts.DEFAULT_COLOUR, Consts.DEFAULT_COLOUR, Consts.DEFAULT_COLOUR, 0, texture);
    }

    public Material(Texture texture,float reflectance) {
        this(Consts.DEFAULT_COLOUR, Consts.DEFAULT_COLOUR, Consts.DEFAULT_COLOUR, 0, texture);
    }


    public Material(Vector4f ambientColour, Vector4f diffuseColour, Vector4f specularColor, float reflectance, Texture texture) {
        this.ambientColour = ambientColour;
        this.diffuseColour = diffuseColour;
        this.specularColor = specularColor;
        this.reflectance = reflectance;
        this.texture = texture;
    }

    public Vector4f getAmbientColour() {
        return ambientColour;
    }

    public void setAmbientColour(Vector4f ambientColour) {
        this.ambientColour = ambientColour;
    }

    public Vector4f getDiffuseColour() {
        return diffuseColour;
    }

    public void setDiffuseColour(Vector4f diffuseColour) {
        this.diffuseColour = diffuseColour;
    }

    public Vector4f getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector4f specularColor) {
        this.specularColor = specularColor;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean hasTexture() {
        return texture != null;
    }
}
