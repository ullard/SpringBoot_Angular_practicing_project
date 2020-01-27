import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsPassChangeComponent } from './settings-pass-change.component';

describe('SettingsPassChangeComponent', () => {
  let component: SettingsPassChangeComponent;
  let fixture: ComponentFixture<SettingsPassChangeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingsPassChangeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsPassChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
