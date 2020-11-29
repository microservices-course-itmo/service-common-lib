package com.wine.to.up.commonlib.security;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
  private Long id;
  private String role;
}
