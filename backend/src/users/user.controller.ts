import { Body, Controller, Post, HttpCode, HttpStatus } from '@nestjs/common';
import { UserService } from './user.service';
import { NewUserDTO } from './dto/newUser.dto';

@Controller('user')
export class UserController {
  constructor(private userService: UserService) {}

  @Post('create')
  @HttpCode(HttpStatus.CREATED)
  async create(@Body() newUserDto: NewUserDTO) {
    console.log('create user. user.controller.ts. newUserDto: ', newUserDto);
    await this.userService.create(newUserDto);
    return {
      status: HttpStatus.CREATED,
      message: 'User created successfully'
    }
    }
}
