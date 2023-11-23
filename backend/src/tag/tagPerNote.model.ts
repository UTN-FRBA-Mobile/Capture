import { Column, ForeignKey, Model, Table } from 'sequelize-typescript';
import { Note } from '../note/note.model';
import { Tag } from './tag.model';

@Table({ timestamps: true })
export class TagPerNote extends Model {
  @ForeignKey(() => Note)
  @Column
  noteId: number;

  @ForeignKey(() => Tag)
  @Column
  tagName: string;
}
