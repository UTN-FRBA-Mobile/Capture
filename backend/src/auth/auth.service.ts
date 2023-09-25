import { Injectable } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { UserService } from '../users/user.service';
import { LoginResponse } from './interfaces/loginResponse.interface';
import { User } from '../users/user.model';
import { JwtPayload } from './interfaces/jwtPayload.interface';

@Injectable()
export class AuthService {
  constructor(
    private readonly userService: UserService,
    private readonly jwtService: JwtService,
  ) {}

  async validateUser(username: string, password: string) {
    const user = await this.userService.findByUsername(username);

    if (user && user.password === password) {
      return user;
    }

    return undefined;
  }

  login(user: User): LoginResponse {
    const payload: JwtPayload = {
      username: user.username,
    };

    return {
      token: this.jwtService.sign(payload),
    };
  }
}
