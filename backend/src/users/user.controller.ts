import { Body, Controller, Post } from '@nestjs/common';
import { UserService } from './user.service';
import { NewUserDTO } from './dto/newUser.dto';

@Controller('user')
export class UserController {
  constructor(private userService: UserService) {}

  @Post('create')
  async create(@Body() newUserDto: NewUserDTO) {
    await this.userService.create(newUserDto);
    return 'User created';
  }
}
