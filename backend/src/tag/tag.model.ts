import {
  Table,
  Model,
  Column,
  BelongsToMany,
  ForeignKey,
} from 'sequelize-typescript';
import { Note } from '../note/note.model';
import { TagPerNote } from './tagPerNote.model';
import { User } from '../users/user.model';

@Table({ timestamps: false })
export class Tag extends Model {
  @Column
  name: string;

  @ForeignKey(() => User)
  @Column
  username: string;

  @Column
  colour: string;

  @BelongsToMany(() => Note, () => TagPerNote)
  notes: Note[];
}
