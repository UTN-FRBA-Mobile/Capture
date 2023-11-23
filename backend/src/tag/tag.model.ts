import {
  Table,
  Model,
  Column,
  PrimaryKey,
  BelongsToMany,
} from 'sequelize-typescript';
import { Note } from '../note/note.model';
import { TagPerNote } from './tagPerNote.model';

@Table({ timestamps: true })
export class Tag extends Model {
  @PrimaryKey
  @Column
  name: string;

  @Column
  colour: string;

  @BelongsToMany(() => Note, () => TagPerNote)
  notes: Note[];
}
