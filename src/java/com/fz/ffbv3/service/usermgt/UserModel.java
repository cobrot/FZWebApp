/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fz.ffbv3.service.usermgt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Agustinus Ignat
 */
public class UserModel
{
  @SerializedName("UserData")
  @Expose
  private UserData userData;

  public UserData getUserData() {
  return userData;  
  }

  public void setUserData(UserData userData) {
  this.userData = userData; 
  }
}
