package com.macsoftech.ekart.model.sizes;

public class LengthModel {
   private String length;

   public String getLength() {
      return length;
   }

   public void setLength(String length) {
      this.length = length;
   }

   @Override
   public String toString() {
      return getLength();
   }
}
