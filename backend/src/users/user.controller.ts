import { Body, Controller, Post, HttpCode, HttpStatus } from '@nestjs/common';
import { UserService } from './user.service';
import { NewUserDTO } from './dto/newUser.dto';
import { UserSignupResponse } from './interfaces/userResponse.interface';

@Controller('user')
export class UserController {
  constructor(private userService: UserService) { }

  @Post('create')
  @HttpCode(HttpStatus.CREATED)
  async create(@Body() newUserDto: NewUserDTO): Promise<UserSignupResponse> {
    console.log('create user. user.controller.ts. newUserDto: ', newUserDto);
    await this.userService.create(newUserDto);

    // Return an instance of UserSignupResponse
    return {
      username: newUserDto.username,
      // Add userId here if needed
    };
  }
}
