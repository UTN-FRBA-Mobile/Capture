import {
    Table,
    Model,
    Column,
    ForeignKey,
    BelongsTo,
  } from 'sequelize-typescript';
  import { Note } from '../note/note.model';
  
  @Table({ timestamps: true })
  export class Tag extends Model {
    @Column
    name: string;
  
    @ForeignKey(() => Note) // Add this line
    @Column
    noteId: number; // This will be used as the foreign key to associate with Note
  
    @BelongsTo(() => Note)
    note: Note;
  }
  