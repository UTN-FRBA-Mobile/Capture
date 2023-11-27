import {
  Body,
  Controller,
  Post,
  HttpCode,
  HttpStatus,
  ConflictException,
} from '@nestjs/common';
import { UserService } from './user.service';
import { NewUserDTO } from './dto/newUser.dto';
import { UserSignupResponse } from './interfaces/userResponse.interface';

@Controller('user')
export class UserController {
  constructor(private userService: UserService) {}

  @Post('create')
  @HttpCode(HttpStatus.CREATED)
  async create(@Body() newUserDto: NewUserDTO): Promise<UserSignupResponse> {
    const existingUser = await this.userService.findByUsername(
      newUserDto.username,
    );

    if (existingUser) {
      throw new ConflictException();
    }

    await this.userService.create(newUserDto);

    // Return an instance of UserSignupResponse
    return {
      username: newUserDto.username,
      // Add userId here if needed
    };
  }
}
