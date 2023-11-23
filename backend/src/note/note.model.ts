import {
  Table,
  Model,
  Column,
  BelongsTo,
  AllowNull,
  BelongsToMany,
} from 'sequelize-typescript';
import { User } from '../users/user.model';
import { Tag } from '../tag/tag.model';
import { TagPerNote } from '../tag/tagPerNote.model';

@Table({ timestamps: true })
export class Note extends Model {
  @BelongsTo(() => User, 'username')
  user: User;

  @Column
  title: string;

  @Column
  content: string;

  @AllowNull
  @Column
  imageName: string;

  @AllowNull
  @Column
  audioName: string;

  @BelongsToMany(() => Tag, () => TagPerNote)
  tags: Tag[];
}
