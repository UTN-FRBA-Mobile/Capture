import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/sequelize';
import { User } from './user.model';
import { NewUserDTO } from './dto/newUser.dto';

@Injectable()
export class UserService {
  constructor(
    @InjectModel(User)
    private userModel: typeof User,
  ) {}

  async create(newUserDto: NewUserDTO) {
    return this.userModel.create({ ...newUserDto });
  }

  async findByUsername(username: string) {
    return this.userModel.findByPk(username);
  }
}
