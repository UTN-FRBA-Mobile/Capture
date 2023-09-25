import {
  Table,
  Model,
  Column,
  BelongsTo,
  AllowNull,
} from 'sequelize-typescript';
import { User } from '../users/user.model';

@Table({ timestamps: true })
export class Note extends Model {
  @BelongsTo(() => User, 'username')
  user: User;

  @Column
  content: string;

  @AllowNull
  @Column
  imageName: string;
}
